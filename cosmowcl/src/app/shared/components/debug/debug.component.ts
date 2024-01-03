/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-debug',
  templateUrl: './debug.component.html',
  styleUrls: ['./debug.component.scss']
})
export class DebugComponent implements OnInit {

  @Input() title?: string;
  @Input() value?: any;

  hidden = true;

  constructor() { }

  ngOnInit(): void {
  }

  get effectiveTitle(): string {
    return this.title ?? 'debug data';
  }

  get debugEnabled(): boolean {
    return !!environment.debug;
  }
}
