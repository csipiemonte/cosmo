/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { TestBed } from '@angular/core/testing';

import { WidgetsService } from './widgets.service';

describe('WidgetsService', () => {
  let service: WidgetsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WidgetsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
