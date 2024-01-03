/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Directive, Renderer2, ElementRef, forwardRef, Input } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { ProtoMaskingDirective } from './proto-masking.directive';
import { NG_VALUE_ACCESSOR } from '@angular/forms';


@Directive({
    selector: 'input[masking]',
    providers: [[{
        provide: NG_VALUE_ACCESSOR,
        multi: true,
        useExisting: forwardRef(() => MaskingDirective),
    }]],
})
export class MaskingDirective extends ProtoMaskingDirective {

    @Input() masking: string | null | RegExp = null;

    @Input() regexpModifiers: string | null = null;

    constructor(
        private ngxLogger: NGXLogger,
        private renderer: Renderer2,
        private elementRef: ElementRef ) {

        super(ngxLogger, renderer, elementRef);
    }

    get regex(): RegExp | null {
        if (!this.masking) {
            return null;
        }
        if (typeof this.masking === 'string') {
            return new RegExp(this.masking, this.regexpModifiers ?? 'giu');
        }
        return this.masking;
    }

    parseInput(value: string | undefined | null) {
        const r = this.regex;
        if (!r) {
            return value;
        }

        if (this.masking) {
            if ( typeof value === 'string') {
                const v: string = value.replace(r, '');
                if (v?.length) {
                    return v;
                } else {
                    return '';
                }
            } else {
                throw new Error('Cannot parse a non string value');
            }

        } else {
            return value;
        }
    }

}
