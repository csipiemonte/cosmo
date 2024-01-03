/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ImmagineWrapperComponent } from './immagine-wrapper.component';

describe('ImmagineWrapperComponent', () => {
  let component: ImmagineWrapperComponent;
  let fixture: ComponentFixture<ImmagineWrapperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ImmagineWrapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImmagineWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
