/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeployProcessoComponent } from './deploy-processo.component';

describe('DeployProcessoComponent', () => {
  let component: DeployProcessoComponent;
  let fixture: ComponentFixture<DeployProcessoComponent>;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      declarations: [ DeployProcessoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeployProcessoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
