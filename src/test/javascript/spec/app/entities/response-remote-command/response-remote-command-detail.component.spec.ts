/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { RemotnitoringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ResponseRemoteCommandDetailComponent } from '../../../../../../main/webapp/app/entities/response-remote-command/response-remote-command-detail.component';
import { ResponseRemoteCommandService } from '../../../../../../main/webapp/app/entities/response-remote-command/response-remote-command.service';
import { ResponseRemoteCommand } from '../../../../../../main/webapp/app/entities/response-remote-command/response-remote-command.model';

describe('Component Tests', () => {

    describe('ResponseRemoteCommand Management Detail Component', () => {
        let comp: ResponseRemoteCommandDetailComponent;
        let fixture: ComponentFixture<ResponseRemoteCommandDetailComponent>;
        let service: ResponseRemoteCommandService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RemotnitoringTestModule],
                declarations: [ResponseRemoteCommandDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ResponseRemoteCommandService,
                    JhiEventManager
                ]
            }).overrideTemplate(ResponseRemoteCommandDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ResponseRemoteCommandDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ResponseRemoteCommandService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ResponseRemoteCommand(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.responseRemoteCommand).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
