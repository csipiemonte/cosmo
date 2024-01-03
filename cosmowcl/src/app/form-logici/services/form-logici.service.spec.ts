/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { TestBed } from '@angular/core/testing';

import { FormLogiciService } from './form-logici.service';

describe('FormLogiciService', () => {
  let service: FormLogiciService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormLogiciService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
