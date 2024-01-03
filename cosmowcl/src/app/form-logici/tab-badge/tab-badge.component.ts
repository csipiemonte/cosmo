/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit } from '@angular/core';
import { TabBadge } from '../models/tab-status.models';

@Component({
  selector: 'app-tab-badge',
  templateUrl: './tab-badge.component.html',
  styleUrls: ['./tab-badge.component.scss']
})
export class TabBadgeComponent implements OnInit {

  @Input() badge?: TabBadge;
  @Input() text?: string;
  @Input() badgeClass?: string;
  @Input() icon?: string;
  @Input() tooltip?: string;

  constructor() { }

  ngOnInit(): void {
  }

  get render(): boolean {
    return !!(this.effectiveText?.length || this.effectiveIcon?.length || this.effectiveClass?.length || this.effectiveTooltip?.length);
  }

  get effectiveText(): string | null {
    return this.text ?? this.badge?.text ?? null;
  }

  get effectiveClass(): string | null {
    return this.badgeClass ?? this.badge?.class ?? null;
  }

  get effectiveIcon(): string | null {
    return this.text ?? this.badge?.icon ?? null;
  }

  get effectiveTooltip(): string | null {
    return this.tooltip ?? this.badge?.tooltip ?? null;
  }

  get hasText(): boolean {
    return (!!this.effectiveText?.length || !!this.effectiveIcon?.length) ?? false;
  }
}
