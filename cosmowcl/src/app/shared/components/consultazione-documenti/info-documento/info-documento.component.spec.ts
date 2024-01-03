/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InfoDocumentoComponent } from './info-documento.component';

describe('InfoDocumentoComponent', () => {
  let component: InfoDocumentoComponent;
  let fixture: ComponentFixture<InfoDocumentoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InfoDocumentoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InfoDocumentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
