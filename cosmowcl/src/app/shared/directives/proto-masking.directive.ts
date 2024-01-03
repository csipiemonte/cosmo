/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Renderer2, ElementRef, HostListener, Directive } from '@angular/core';
import { ControlValueAccessor } from '@angular/forms';
import { NGXLogger } from 'ngx-logger';


@Directive()
export abstract class ProtoMaskingDirective implements ControlValueAccessor {

    private innerValue: any = null;

    onChange: any = () => {};

    onTouch: any = () => {};

    constructor(
        private parentLogger: NGXLogger,
        private parentRenderer: Renderer2,
        private parentElementRef: ElementRef ) {
        this.parentLogger.trace('constructor');
    }

    abstract parseInput(value: any): any;

    @HostListener('input', ['$event']) hostListenerInput($event: any) {
        this._handleInput($event);
    }

    @HostListener('blur', ['$event']) hostListenerBlur($event: any) {
        this.onTouch($event);
    }

    _onChange(value: any) {
        this.parentLogger.trace('_onChange', value);
        this.onChange(value);
    }

    _onTouch(value: any) {
        this.parentLogger.trace('_onTouch', value);
        this.onTouch(value);
    }

    _handleInput(event: any): void {
        const value = event.target.value;
        this.parentLogger.trace('_handleInput', value);

        const v = this._parseInput(value);

        this._updateInnerValue(v);
        this._updateView(v);
        this._onChange(v);
    }

    _parseInput(value: any) {
        let output;
        if (value === null || typeof value === 'undefined') {
            output = value;
        } else {
            output = this.parseInput(value);
        }

        this.parentLogger.trace('_parseInput [' + value + '] => [' + output + ']');
        return output;
    }

    _updateView(value: any) {
        this.parentLogger.trace('_updateView', value);
        this.parentRenderer.setProperty(this.parentElementRef.nativeElement, 'value', value);
    }

    _updateInnerValue(value: any) {
        const prev = this.innerValue;
        this.innerValue = value;
        this.parentLogger.trace('_updateInnerValue [' + prev + '] => [' + value + ']');
    }

    get value(): any {
        this.parentLogger.trace('get value');
        return this.innerValue;
    }

    // this value is updated by programmatic changes
    set value(val) {
        this.parentLogger.trace('set value', val);
        if (val !== this.innerValue) {
            const p = this._parseInput(val);
            this._updateInnerValue(p);
            this._updateView(p);
            this._onChange(p);
            this._onTouch(p);
        }
    }

    // this method sets the value programmatically
    writeValue(value: any) {
        this.parentLogger.trace('writeValue', value);
        const p = this._parseInput(value);
        this._updateInnerValue(p);
        this._updateView(p);

        if (p !== this.innerValue) {
            this._onChange(p);
        }
    }

    setDisabledState(isDisabled: boolean): void {
        this.parentLogger.trace('setDisabledState', isDisabled);
        this.parentRenderer.setProperty(this.parentElementRef.nativeElement, 'disabled', isDisabled);
    }

    // upon UI element value changes, this method gets triggered
    registerOnChange(fn: any) {
        this.parentLogger.trace('registerOnChange', fn);
        this.onChange = fn;
    }

    // upon touching the element, this method gets triggered
    registerOnTouched(fn: any) {
        this.parentLogger.trace('registerOnTouched', fn);
        this.onTouch = fn;
    }

}
