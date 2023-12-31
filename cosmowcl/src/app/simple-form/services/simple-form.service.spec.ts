/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { TestBed } from '@angular/core/testing';

import { SimpleFormService } from './simple-form.service';

describe('SimpleFormService', () => {
  let service: SimpleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SimpleFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
