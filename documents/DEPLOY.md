# Deploy ParlorPlace

This following guide document provides information on how to deploy *ParlorPlace*.
The first section will cover using the provided docker containers, in order to get the backend up and running in a quick fashion (for testing or evaluation).

The further sections cover the process of permanently deploying it to a *CentOS 8* server.

## Docker

This simple approach allows for getting *ParlorPlace* up and running quicky.
In order to serve the Angular frontend, simply run `ng serve`.
Creating a specialized Docker image for serving the frontend was omitted due to the simplicity of serving it, but can be created at a later point in time if demand for it exists.

For the backend we refer to the [Docker Repository](https://hub.docker.com/repository/docker/fschoen/parlorplace), on which you will find images for deploying the backend.
Pull a Docker image and run it using e.g. the command `docker pull fschoen/parlorplace:v1.0-production` (alternatively use `parlorplace:v1.0-setup`).
The production version does not create any user accounts, while the setup version creates predefined user accounts, which can be used to test the application.

## CentOS Server

This section will cover permanently deploying the application to a server running *CentOS 8*.

Start by running `ng build` to build the frontend, and `mvn install` to build the backend.
Copy the generated files onto the CentOS server.

### Java

*ParlorPlace* uses `OpenJDK 16`, which is what we need to install first.
Make sure to `cd` into the home directory, and run the following commands, which download and extract Java:

```
curl -O https://download.java.net/java/GA/jdk16.0.2/d4a915d82b4c4fbb9bde534da945d746/7/GPL/openjdk-16.0.2_linux-x64_bin.tar.gz

sudo tar xvf openjdk-16.0.2_linux-x64_bin.tar.gz

sudo mv jdk-16.0.2 /opt/
```

The last command moves the Java installation into the `/opt` directory, thus we have to adjust the `PATH` variable, in order for the OS to find our installation.
Edit (or create if it does not exist) the file `java.sh` via the following command: `sudo nano /etc/profile.d/java.sh`.
It needs to include the following information:

```
JAVA_HOME=/opt/jdk-16.0.2
export JAVA_HOME
export PATH=$PATH:$JAVA_HOME/bin
```

After adjusting all these settings, we can check for a successful installation of Java using `java --version`, it should yield information about the Java installation.

<!-- ## Configure System Service

Create file under `usr/lib/systemd/system` named `ppbackend.service` with following content:

```
[Unit]
Description=ParlorPlace Backend Server Daemon

[Service]
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=production
/root/app/ppbackend.jar
User=root
WorkingDirectory=/root

[Install]
WantedBy=multi-user.target
```

## Create executable file

Create file with `java -jar -Dspring.profiles.active=production ppbackend.jar`, make it runnable and run it with `./start.sh` -->

## Apache

The Apache installation is responsible for serving both our frontend and backend.
Install it using `sudo dnf install httpd`.

Make sure that the firewall is installed by running `dnf install firewalld -y`, and starting it with `systemctl start firewalld`.
We now need to open the firewall to allow for serving of HTTP and HTTPS.
We can do this by issuing the following commands:

```
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --permanent --add-port=443/tcp
sudo firewall-cmd --reload
```

After that we can start Apache, and check if everything is running correctly.
Furthermore, checking in the browser for `localhost` (or the IP of the server) should yield a test page.

```
sudo systemctl start httpd
sudo systemctl status httpd
```

In order to restart the Apache service (after making some changes to the system), use `sudo systemctl restart httpd`.

### Frontend

We now want to setup Virtual Hosts, which allow us to host multiple sites (or in our case, both the backend and frontend of *ParlorPlace*) on the same server.
we start by creating a directory for whichever service we are trying to create, using `sudo mkdir -p /var/www/parlorplace.fschoen.com/html`.
It is important to assign the right ownership of the folder, using `sudo chown -R $USER:apache /var/www/` and `sudo chmod -R 755 /var/www`.
At this point in time it can be advantageous to create a sample `index.html` file, in order to check if the service is running correctly.

We move onto creating two new directories, which will later be used to link the Apache config files into the executed configuration.
Create them using `sudo mkdir /etc/httpd/sites-available /etc/httpd/sites-enabled`.
Furthermore, we need to edit the Apache configuration in order to tell it to look into these folders.
Run `sudo nano /etc/httpd/conf/httpd.conf`, and append the following line, which will tell Apache to process all configuration files in this folder.

```
IncludeOptional sites-enabled/*.conf
```

In the next step we create the actual configuration, by editing `sudo nano /etc/httpd/sites-available/parlorplace.fschoen.com.conf` (or equivalent files).
The following shows a valid configuration, that is used to serve the frontend:

```
<VirtualHost *:80>

    ServerName parlorplace.fschoen.com
    DocumentRoot /var/www/parlorplace.fschoen.com/html
    ErrorLog /var/www/parlorplace.fschoen.com/log/error.log
    CustomLog /var/www/parlorplace.fschoen.com/log/requests.log combined

    # Redirect
    RewriteEngine on
    RewriteCond %{SERVER_NAME} =fschoen.com [OR]
    RewriteCond %{SERVER_NAME} =parlorplace.fschoen.com
    RewriteRule ^ https://%{SERVER_NAME}%{REQUEST_URI} [END,NE,R=permanent]

</VirtualHost>
```

Lastly, we have to link the created configuration file into the fitting folder, using `sudo ln -s /etc/httpd/sites-available/parlorplace.fschoen.com.conf /etc/httpd/sites-enabled/parlorplace.fschoen.com.conf`.

> **NOTE:**  At the moment SELinux is disabled using `sudo setenforce 0`. This could potentially change in the future.

In order to update the actual files of the frontend, a script under `home/frontend/copy.sh` was created.
We can run it by issuing the command `sudo ./copy.sh`, which will copy all files of `home/frontend/parlorplace` to the frontend folder of Apache.

For testing purposes it can be beneficial to edit the `hosts` file, in order to circumvent actual DNS resolution in favour of a fixed one.
If we edit `sudo nano /etc/hosts` and add the following entry, the domain will point to our local server.

```
192.168.0.70 parlorplace.fschoen.com
```

### Backend

The procedure for the backend is similar as for the frontend; create a new folder using `sudo mkdir -p /var/www/parlorplaceapi.fschoen.com/html`.
Do not forget to update the permissions!
Assuming we already create the `sites-available` and `sites-enabled` folders, we can move onto creating the actual config file by issuing the command `sudo nano /etc/httpd/sites-available/parlorplaceapi.fschoen.com.conf`.
The following represents a valid configuration for the backend:

```
<VirtualHost *:80>

    ServerName parlorplaceapi.fschoen.com
    DocumentRoot /var/www/parlorplaceapi.fschoen.com/html
    ErrorLog /var/www/parlorplaceapi.fschoen.com/log/error.log
    CustomLog /var/www/parlorplaceapi.fschoen.com/log/requests.log combined

    # Redirect
    RewriteEngine on
    RewriteCond %{SERVER_NAME} =fschoen.com [OR]
    RewriteCond %{SERVER_NAME} =parlorplaceapi.fschoen.com
    RewriteRule ^ https://%{SERVER_NAME}%{REQUEST_URI} [END,NE,R=permanent]

</VirtualHost>
```

## Security

In order to secure the server, we have to do the following things:

- Obtain a certificate for the website (done using `Certbot`)
  - In order for this to work access to the DNS records of the domain has to be available, e.g. for setting a challenge
- Tell apache to use these certificates on the port 443

### Certificate

This brief section will cover how to obtain the certificates.
For this, we use `Certbot`, installation will not be covered here.

At any point in time, `certbot certificates` can be used in order to check for any installed certificates.
This should yield an entry for `*.fschoen.com`, with the name of "fschoen.com", which is used for both the frontend and the backend, since it is a wildcard certificate (meaning that any subdomain of fschoen.com is covered by it).
The installation of the certificate will be covered in the Apache session, the following will include steps for refreshing it.

```
sudo certbot certonly -d *.fschoen.com
```

Select `1: Apache Web Server plugin (apache)`, and then `2: Renew & replace the certificate`.

> **NOTE:**  At the point of writing refreshing this has actually never been tested. Continue writing this when this point in time comes.

### Apache

In order to secure both the frontend and backend, we have to include the certificates in Apache.
Certbot has the capacity to do this automatically, but some of the settings seem to crash, which is why we opt to do it manually.
The following excerpts represent the configuration for both the front- and backend with activated security:

For `parlorplace.fschoen.com.ssl.conf`:

```
<IfModule mod_ssl.c>
<VirtualHost *:443>

    ServerName parlorplace.fschoen.com
    DocumentRoot /var/www/parlorplace.fschoen.com/html
    ErrorLog /var/www/parlorplace.fschoen.com/log/error.log
    CustomLog /var/www/parlorplace.fschoen.com/log/requests.log combined

    SSLEngine on

    # Certbot
    SSLCertificateFile /etc/letsencrypt/live/fschoen.com/fullchain.pem
    SSLCertificateKeyFile /etc/letsencrypt/live/fschoen.com/privkey.pem
    SSLCertificateChainFile /etc/letsencrypt/live/fschoen.com/chain.pem

    # Angular
    RewriteEngine On

    # If an existing asset or directory is requested go to it as it is
    RewriteCond %{DOCUMENT_ROOT}%{REQUEST_URI} -f [OR]
    RewriteCond %{DOCUMENT_ROOT}%{REQUEST_URI} -d
    RewriteRule ^ - [L]

    # If the requested resource doesn't exist, use index.html
    RewriteRule ^ /index.html

</VirtualHost>
</IfModule>
```

For `parlorplaceapi.fschoen.com.ssl.conf`:

```
<IfModule mod_ssl.c>
<VirtualHost *:443>

    ServerName parlorplaceapi.fschoen.com
    DocumentRoot /var/www/parlorplaceapi.fschoen.com/html
    ErrorLog /var/www/parlorplaceapi.fschoen.com/log/error.log
    CustomLog /var/www/parlorplaceapi.fschoen.com/log/requests.log combined

    Header unset Access-Control-Allow-Origin
    Header always set Access-Control-Allow-Origin "https://parlorplace.fschoen.com"
    Header always set Access-Control-Allow-Headers "Content-Type, Authorization"
    Header always set Access-Control-Allow-Methods "POST, GET, OPTIONS, DELETE, PUT"
    Header always set Access-Control-Expose-Header "Content-Security-Policy, Location"
    Header always set Access-Control-Max-Age "600"

    SSLEngine on

    # Certbot
    SSLCertificateFile /etc/letsencrypt/live/fschoen.com/fullchain.pem
    SSLCertificateKeyFile /etc/letsencrypt/live/fschoen.com/privkey.pem
    SSLCertificateChainFile /etc/letsencrypt/live/fschoen.com/chain.pem

    # Backend
    RewriteEngine On

    # Redirect to Frontend
    RewriteCond %{REQUEST_URI} !^/api
    RewriteCond %{REQUEST_URI} !^/comm
    RewriteRule ^(.*)$ https://parlorplace.fschoen.com$1 [L,R=301]

    # Backend
    ProxyPreserveHost On
    SSLProxyEngine On

    ProxyPass /api/ http://127.0.0.1:1616/
    ProxyPassReverse /api/ http://127.0.0.1:1616/

    ProxyPass /comm/ ws://127.0.0.1:1616/
    ProxyPassReverse /comm/ ws://127.0.0.1:1616/

</VirtualHost>
</IfModule>
```

Just a brief explanation of the files:

The frontend file simply contains predefined Angular directives, which were taken from the official website, and serves straight up HTTP/S.

The backend file has a redirection to the frontend for any request not containing either `/api` or `/comm`, and a ProxyPass in order to hand off requests to the locally running backend.

## Useful Links

- [Apache on CentOS 8](https://www.digitalocean.com/community/tutorials/how-to-install-the-apache-web-server-on-centos-8)
- [Secure Apache](https://www.digitalocean.com/community/tutorials/how-to-secure-apache-with-let-s-encrypt-on-centos-8)
- [Apache WebSocket Configuration](https://stackoverflow.com/questions/27526281/websockets-and-apache-proxy-how-to-configure-mod-proxy-wstunnel)
- [Let's Encrypt Wildcard](https://www.digitalocean.com/community/tutorials/how-to-create-let-s-encrypt-wildcard-certificates-with-certbot)
