import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RemotnitoringSharedModule } from '../../shared';
import {
    RequestRemoteCommandService,
    RequestRemoteCommandPopupService,
    RequestRemoteCommandComponent,
    RequestRemoteCommandDetailComponent,
    RequestRemoteCommandDialogComponent,
    RequestRemoteCommandPopupComponent,
    RequestRemoteCommandDeletePopupComponent,
    RequestRemoteCommandDeleteDialogComponent,
    requestRemoteCommandRoute,
    requestRemoteCommandPopupRoute,
    RequestRemoteCommandResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...requestRemoteCommandRoute,
    ...requestRemoteCommandPopupRoute,
];

@NgModule({
    imports: [
        RemotnitoringSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RequestRemoteCommandComponent,
        RequestRemoteCommandDetailComponent,
        RequestRemoteCommandDialogComponent,
        RequestRemoteCommandDeleteDialogComponent,
        RequestRemoteCommandPopupComponent,
        RequestRemoteCommandDeletePopupComponent,
    ],
    entryComponents: [
        RequestRemoteCommandComponent,
        RequestRemoteCommandDialogComponent,
        RequestRemoteCommandPopupComponent,
        RequestRemoteCommandDeleteDialogComponent,
        RequestRemoteCommandDeletePopupComponent,
    ],
    providers: [
        RequestRemoteCommandService,
        RequestRemoteCommandPopupService,
        RequestRemoteCommandResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RemotnitoringRequestRemoteCommandModule {}
