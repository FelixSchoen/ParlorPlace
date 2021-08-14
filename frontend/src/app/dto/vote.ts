import {VoteState} from "../enums/votestate";
import {VoteType} from "../enums/votetype";
import {EnumValue} from "@angular/compiler-cli/src/ngtsc/partial_evaluator";
import {Player} from "./player";

export abstract class Vote {

  protected constructor(public id: number,
                        public voteState: VoteState,
                        public voteType: VoteType,
                        public voteDescriptor: EnumValue,
                        public voteCollectionMap: Map<Player, VoteCollection>,
                        public outcome: Set<any>,
                        public outcomeAmount: number,
                        public endTime: number) {
  }

}

export abstract class VoteCollection {

  protected constructor(public amountVotes: number,
                        public allowAbstain: boolean,
                        public abstain: boolean,
                        public subjects: Set<any>,
                        public selection: Set<any>) {
  }

}
