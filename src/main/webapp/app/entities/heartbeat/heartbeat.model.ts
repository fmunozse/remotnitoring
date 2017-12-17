import { BaseEntity } from './../../shared';

export class Heartbeat implements BaseEntity {
    constructor(
        public id?: number,
        public timestamp?: any,
        public ip?: string,
        public node?: BaseEntity,
    ) {
    }
}
