import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Node } from './node.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class NodeService {

    private resourceUrl = SERVER_API_URL + 'api/nodes';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(node: Node): Observable<Node> {
        const copy = this.convert(node);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(node: Node): Observable<Node> {
        const copy = this.convert(node);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Node> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Node.
     */
    private convertItemFromServer(json: any): Node {
        const entity: Node = Object.assign(new Node(), json);
        entity.renewDay = this.dateUtils
            .convertLocalDateFromServer(json.renewDay);
        return entity;
    }

    /**
     * Convert a Node to a JSON which can be sent to the server.
     */
    private convert(node: Node): Node {
        const copy: Node = Object.assign({}, node);
        copy.renewDay = this.dateUtils
            .convertLocalDateToServer(node.renewDay);
        return copy;
    }
}
