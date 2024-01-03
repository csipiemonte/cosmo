/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { environment } from "src/environments/environment";

export class CaricamentoInCorsoIconPicker {

    private static initialized = false;

    private static STANDARD_ICONS = [
        'fas fa-user-astronaut',
        'fas fa-meteor',
        'far fa-moon',
        'fas fa-robot',
        'fas fa-rocket',
        'fas fa-satellite',
        'fas fa-satellite-dish',
        'fas fa-globe-europe',
    ];

    private static ICON_SETS: {[key: string]: string[]} = {
        standard: [
            ...CaricamentoInCorsoIconPicker.STANDARD_ICONS,
        ],
        '15ag': [
            'fas fa-cocktail'
        ],
        xmas: [
            'fas fa-sleigh',
            'fas fa-gifts',
            'fas fa-holly-berry',
            'fas fa-candy-cane',
            'fas fa-snowman',
        ],
        yend: [
            'fas fa-glass-cheers',
            'fas fa-snowman',
            'fas fa-mug-hot',
        ],
        hwee: [
            'fas fa-cat',
            'fas fa-crow',
            'fas fa-broom',
            'fas fa-ghost',
            'fas fa-ghost',
            'fas fa-ghost',
            'fas fa-spider',
            'fas fa-spider',
        ],
        labor: [
            'fas fa-tools',
            'fas fa-toolbox',
            'fas fa-hammer',
            'fas fa-screwdriver',
            'fas fa-wrench',
        ],
        sval: [
            ...CaricamentoInCorsoIconPicker.STANDARD_ICONS,
            'fas fa-heart',
            'fas fa-kiss-wink-heart',
        ],
        test: ['fas fa-brain'],
    };

    private static PERDAY_ICONS: {[key: string]: string} = {
        '01-01,01-02': 'yend',
        '02-14': 'sval',
        '05-01': 'labor',
        '08-15': '15ag',
        '10-29,10-31': 'hwee',
        '12-15,12-28': 'xmas',
        '12-29,12-31': 'yend',
    };

    private static COMPILED_DAYS_MAP: {[key: string]: string} = {};

    public static getAllKnown(): string[] {
        const out: string[] = [];
        for (const serie of Object.values(this.ICON_SETS)) {
            for (const icon of serie) {
                if (!out.includes(icon)) {
                    out.push(icon);
                }
            }
        }

        return out;
    }

    public static get(): string {
        this.initialize();
        let setName = '';
        if (environment.production) {
            setName = 'standard';
        } else {
            const dateKey = new Date().toISOString().substr(5, 5);
            setName = this.COMPILED_DAYS_MAP[dateKey] ?? 'standard';
        }

        const set = this.ICON_SETS[setName];
        return set[Math.floor(Math.random() * set.length)];
    }

    private static initialize(): void {
        if (this.initialized) {
            return;
        }

        for (const period of Object.keys(this.PERDAY_ICONS)) {
            const v = this.PERDAY_ICONS[period];
            const expanded = this.period(period);
            for (const itemExpanded of expanded) {
                this.COMPILED_DAYS_MAP[itemExpanded] = v;
            }
        }

        this.initialized = true;
    }

    private static period(def: string): string[] {
        const splittedDef = def.split(',');
        const start = splittedDef[0];
        const end = splittedDef.length >= 2 ? splittedDef[1] : null;
        const out: string[] = [];
        const splittedStart = start.split('-');
        const splittedEnd = end ? end.split('-') : null;
        const thisYear = new Date().getFullYear();
        const startDate = new Date(thisYear, parseInt(splittedStart[0], 10) - 1, parseInt(splittedStart[1], 10), 12, 0, 0);
        const endDate = splittedEnd ? new Date(thisYear, parseInt(splittedEnd[0], 10) - 1, parseInt(splittedEnd[1], 10), 12, 0, 0) : null;
        let currDate = startDate;
        do {
            out.push(currDate.toISOString().substr(5, 5));
            currDate = new Date(currDate.getTime() + 1000 * 60 * 60 * 24);
        } while (endDate && currDate.getTime() <= endDate?.getTime());
        return out;
    }
}
