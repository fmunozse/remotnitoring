import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RemotnitoringNodeModule } from './node/node.module';
import { RemotnitoringHeartbeatModule } from './heartbeat/heartbeat.module';
import { RemotnitoringRequestRemoteCommandModule } from './request-remote-command/request-remote-command.module';
import { RemotnitoringResponseRemoteCommandModule } from './response-remote-command/response-remote-command.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        RemotnitoringNodeModule,
        RemotnitoringHeartbeatModule,
        RemotnitoringRequestRemoteCommandModule,
        RemotnitoringResponseRemoteCommandModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RemotnitoringEntityModule {}
