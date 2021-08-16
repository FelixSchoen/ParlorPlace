import {VoteState} from "../enums/votestate";
import {VoteType} from "../enums/votetype";
import {EnumValue} from "@angular/compiler-cli/src/ngtsc/partial_evaluator";

export abstract class Vote<T, C extends VoteCollection<T>> {

  protected constructor(public id: number,
                        public voteState: VoteState,
                        public voteType: VoteType,
                        public voteDescriptor: EnumValue,
                        public voteCollectionMap: [number, C][],
                        public outcome: Set<T>,
                        public outcomeAmount: number,
                        public endTime: number) {
  }

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
