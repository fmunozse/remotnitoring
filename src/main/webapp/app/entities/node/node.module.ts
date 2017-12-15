import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RemotnitoringSharedModule } from '../../shared';
import { RemotnitoringAdminModule } from '../../admin/admin.module';
import {
    NodeService,
    NodePopupService,
    NodeComponent,
    NodeDetailComponent,
    NodeDialogComponent,
    NodePopupComponent,
    NodeDeletePopupComponent,
    NodeDeleteDialogComponent,
    nodeRoute,
    nodePopupRoute,
    NodeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...nodeRoute,
    ...nodePopupRoute,
];

@NgModule({
    imports: [
        RemotnitoringSharedModule,
        RemotnitoringAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        NodeComponent,
        NodeDetailComponent,
        NodeDialogComponent,
        NodeDeleteDialogComponent,
        NodePopupComponent,
        NodeDeletePopupComponent,
    ],
    entryComponents: [
        NodeComponent,
        NodeDialogComponent,
        NodePopupComponent,
        NodeDeleteDialogComponent,
        NodeDeletePopupComponent,
    ],
    providers: [
        NodeService,
        NodePopupService,
        NodeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RemotnitoringNodeModule {}
