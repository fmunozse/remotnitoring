import { BaseEntity, User } from './../../shared';

export class Node implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public secret?: any,
        public renewDay?: any,
        public model?: string,
        public user?: User,
    ) {
    }
}
