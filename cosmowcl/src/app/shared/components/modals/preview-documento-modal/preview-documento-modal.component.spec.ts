/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewDocumentoModalComponent } from './preview-documento-modal.component';

describe('PreviewDocumentoModalComponent', () => {
  let component: PreviewDocumentoModalComponent;
  let fixture: ComponentFixture<PreviewDocumentoModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreviewDocumentoModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreviewDocumentoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
