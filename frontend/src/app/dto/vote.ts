import {VoteState} from "../enums/vote-state";
import {VoteType} from "../enums/vote-type";
import {EnumValue} from "@angular/compiler-cli/src/ngtsc/partial_evaluator";
import {Player} from "./player";

export abstract class Vote<P extends Player, T, C extends VoteCollection<T>> {

  protected constructor(public id: number,
                        public voteState: VoteState,
                        public voteType: VoteType,
                        public voteDescriptor: EnumValue,
                        public voters: Set<P>,
                        public voteCollectionMap: [number, C][],
                        public outcome: Set<T>,
                        public outcomeAmount: number,
                        public endTime: number) {
  }



}

export class VoteUtil {

  public static toMap<C extends VoteCollection<any>>(voteCollectionMap: [number, C][]): Map<number, C> {
    let map = new Map<number, C>();
    for (let entry of Object.entries(voteCollectionMap)) {
      map.set(Number(entry[0]), entry[1] as unknown as C);
    }
    return map;
  }

}

export abstract class VoteCollection<T> {

  protected constructor(public amountVotes: number,
                        public allowAbstain: boolean,
                        public abstain: boolean,
                        public subjects: T[],
                        public selection: T[]) {
  }

}
