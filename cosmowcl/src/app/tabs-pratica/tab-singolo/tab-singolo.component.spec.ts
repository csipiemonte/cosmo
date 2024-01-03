/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TabSingoloComponent } from './tab-singolo.component';

describe('TabSingoloComponent', () => {
  let component: TabSingoloComponent;
  let fixture: ComponentFixture<TabSingoloComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TabSingoloComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TabSingoloComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
