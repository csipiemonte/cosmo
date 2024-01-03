import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RowExpansionComponent } from './row-expansion.component';

describe('RowExpansionComponent', () => {
  let component: RowExpansionComponent;
  let fixture: ComponentFixture<RowExpansionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RowExpansionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RowExpansionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
