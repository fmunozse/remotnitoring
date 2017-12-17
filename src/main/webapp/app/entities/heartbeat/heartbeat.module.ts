import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RemotnitoringSharedModule } from '../../shared';
import {
    HeartbeatService,
    HeartbeatPopupService,
    HeartbeatComponent,
    HeartbeatDetailComponent,
    HeartbeatDialogComponent,
    HeartbeatPopupComponent,
    HeartbeatDeletePopupComponent,
    HeartbeatDeleteDialogComponent,
    heartbeatRoute,
    heartbeatPopupRoute,
    HeartbeatResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...heartbeatRoute,
    ...heartbeatPopupRoute,
];

@NgModule({
    imports: [
        RemotnitoringSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        HeartbeatComponent,
        HeartbeatDetailComponent,
        HeartbeatDialogComponent,
        HeartbeatDeleteDialogComponent,
        HeartbeatPopupComponent,
        HeartbeatDeletePopupComponent,
    ],
    entryComponents: [
        HeartbeatComponent,
        HeartbeatDialogComponent,
        HeartbeatPopupComponent,
        HeartbeatDeleteDialogComponent,
        HeartbeatDeletePopupComponent,
    ],
    providers: [
        HeartbeatService,
        HeartbeatPopupService,
        HeartbeatResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RemotnitoringHeartbeatModule {}
