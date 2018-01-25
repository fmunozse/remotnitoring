import { BaseEntity } from './../../shared';

export const enum StatusRemoteCommand {
    'Pending',
    'Completed'
}

export class RequestRemoteCommand implements BaseEntity {
    constructor(
        public id?: number,
        public description?: string,
        public command?: any,
        public status?: StatusRemoteCommand,
        public node?: BaseEntity,
        public responseRemoteCommands?: BaseEntity[],
    ) {
    }
}
