import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ObservableDataComponent } from './observable-data.component';

describe('ObservableDataComponent', () => {
  let component: ObservableDataComponent;
  let fixture: ComponentFixture<ObservableDataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ObservableDataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ObservableDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
