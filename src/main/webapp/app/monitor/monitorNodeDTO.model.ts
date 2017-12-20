import { BaseEntity } from './../shared';

export class MonitorNodeDTO  {
    constructor(
        public nodeId?: number,
        public nodeName?: string,
        public numHeartbeats?: number,
        public lastHeartbeat?: any
    ) {
    }
}
