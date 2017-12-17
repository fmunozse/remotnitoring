import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { HeartbeatComponent } from './heartbeat.component';
import { HeartbeatDetailComponent } from './heartbeat-detail.component';
import { HeartbeatPopupComponent } from './heartbeat-dialog.component';
import { HeartbeatDeletePopupComponent } from './heartbeat-delete-dialog.component';

@Injectable()
export class HeartbeatResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const heartbeatRoute: Routes = [
    {
        path: 'heartbeat',
        component: HeartbeatComponent,
        resolve: {
            'pagingParams': HeartbeatResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Heartbeats'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'heartbeat/:id',
        component: HeartbeatDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Heartbeats'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const heartbeatPopupRoute: Routes = [
    {
        path: 'heartbeat-new',
        component: HeartbeatPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Heartbeats'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'heartbeat/:id/edit',
        component: HeartbeatPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Heartbeats'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'heartbeat/:id/delete',
        component: HeartbeatDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Heartbeats'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
