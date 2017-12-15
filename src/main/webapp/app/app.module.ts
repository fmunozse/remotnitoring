import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { RemotnitoringSharedModule, UserRouteAccessService } from './shared';
import { RemotnitoringAppRoutingModule} from './app-routing.module';
import { RemotnitoringHomeModule } from './home/home.module';
import { RemotnitoringAdminModule } from './admin/admin.module';
import { RemotnitoringAccountModule } from './account/account.module';
import { RemotnitoringEntityModule } from './entities/entity.module';
import { RemotnitoringMonitorModule} from './monitor/monitor.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        RemotnitoringAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        RemotnitoringSharedModule,
        RemotnitoringHomeModule,
        RemotnitoringAdminModule,
        RemotnitoringAccountModule,
        RemotnitoringEntityModule,
        RemotnitoringMonitorModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class RemotnitoringAppModule {}
