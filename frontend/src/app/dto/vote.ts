import {VoteState} from "../enums/votestate";
import {VoteType} from "../enums/votetype";
import {EnumValue} from "@angular/compiler-cli/src/ngtsc/partial_evaluator";
import {Pairing} from "./pairing";

export abstract class Vote<P> {

  public voteCollection: Map<number, VoteCollection<P, any>>;

  protected constructor(public id: number,
                        public voteState: VoteState,
                        public voteType: VoteType,
                        public voteDescriptor: EnumValue,
                        public voteCollectionMap: Pairing<number, VoteCollection<P, any>>[],
                        public outcome: Set<P>,
                        public outcomeAmount: number,
                        public endTime: number) {
    this.voteCollection = new Map<number, VoteCollection<P, any>>();
    for (let entry of this.voteCollectionMap) {
      console.log("now")
      console.log(entry.key)
    }
  }

}

export abstract class VoteCollection<P, T> {

  protected constructor(public amountVotes: number,
                        public allowAbstain: boolean,
                        public abstain: boolean,
                        public subjects: Set<P>,
                        public selection: Set<T>) {
  }

}
