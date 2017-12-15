/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { RemotnitoringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { NodeDetailComponent } from '../../../../../../main/webapp/app/entities/node/node-detail.component';
import { NodeService } from '../../../../../../main/webapp/app/entities/node/node.service';
import { Node } from '../../../../../../main/webapp/app/entities/node/node.model';

describe('Component Tests', () => {

    describe('Node Management Detail Component', () => {
        let comp: NodeDetailComponent;
        let fixture: ComponentFixture<NodeDetailComponent>;
        let service: NodeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RemotnitoringTestModule],
                declarations: [NodeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    NodeService,
                    JhiEventManager
                ]
            }).overrideTemplate(NodeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NodeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NodeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Node(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.node).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
