import { BaseEntity } from './../../shared';

export class ResponseRemoteCommand implements BaseEntity {
    constructor(
        public id?: number,
        public whenExecuted?: any,
        public logResult?: any,
        public codReturn?: string,
        public requestRemoteCommand?: BaseEntity,
    ) {
    }
}
