import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { NodeComponent } from './node.component';
import { NodeDetailComponent } from './node-detail.component';
import { NodePopupComponent } from './node-dialog.component';
import { NodeDeletePopupComponent } from './node-delete-dialog.component';

@Injectable()
export class NodeResolvePagingParams implements Resolve<any> {

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

export const nodeRoute: Routes = [
    {
        path: 'node',
        component: NodeComponent,
        resolve: {
            'pagingParams': NodeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Nodes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'node/:id',
        component: NodeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Nodes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const nodePopupRoute: Routes = [
    {
        path: 'node-new',
        component: NodePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Nodes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'node/:id/edit',
        component: NodePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Nodes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'node/:id/delete',
        component: NodeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Nodes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
