import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RemotnitoringSharedModule } from '../../shared';
import {
    ResponseRemoteCommandService,
    ResponseRemoteCommandPopupService,
    ResponseRemoteCommandComponent,
    ResponseRemoteCommandDetailComponent,
    ResponseRemoteCommandDialogComponent,
    ResponseRemoteCommandPopupComponent,
    ResponseRemoteCommandDeletePopupComponent,
    ResponseRemoteCommandDeleteDialogComponent,
    responseRemoteCommandRoute,
    responseRemoteCommandPopupRoute,
    ResponseRemoteCommandResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...responseRemoteCommandRoute,
    ...responseRemoteCommandPopupRoute,
];

@NgModule({
    imports: [
        RemotnitoringSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ResponseRemoteCommandComponent,
        ResponseRemoteCommandDetailComponent,
        ResponseRemoteCommandDialogComponent,
        ResponseRemoteCommandDeleteDialogComponent,
        ResponseRemoteCommandPopupComponent,
        ResponseRemoteCommandDeletePopupComponent,
    ],
    entryComponents: [
        ResponseRemoteCommandComponent,
        ResponseRemoteCommandDialogComponent,
        ResponseRemoteCommandPopupComponent,
        ResponseRemoteCommandDeleteDialogComponent,
        ResponseRemoteCommandDeletePopupComponent,
    ],
    providers: [
        ResponseRemoteCommandService,
        ResponseRemoteCommandPopupService,
        ResponseRemoteCommandResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RemotnitoringResponseRemoteCommandModule {}
