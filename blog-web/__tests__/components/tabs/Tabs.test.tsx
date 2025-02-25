import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import Tabs from '@/components/tabs/Tabs';

describe('Tabs', () => {
    const tabs = [
        { label: 'Tab 1', content: <div>Content 1</div> },
        { label: 'Tab 2', content: <div>Content 2</div> },
    ];

    it('should render the tabs with the correct labels', () => {
        render(<Tabs tabs={tabs} />);
        expect(screen.getByText('Tab 1')).toBeInTheDocument();
        expect(screen.getByText('Tab 2')).toBeInTheDocument();
    });

    it('should display the content of the first tab by default', () => {
        render(<Tabs tabs={tabs} />);
        expect(screen.getByText('Content 1')).toBeInTheDocument();
        expect(screen.queryByText('Content 2')).not.toBeInTheDocument();
    });

    it('should switch to the second tab when clicked', () => {
        render(<Tabs tabs={tabs} />);
        fireEvent.click(screen.getByText('Tab 2'));
        expect(screen.getByText('Content 2')).toBeInTheDocument();
        expect(screen.queryByText('Content 1')).not.toBeInTheDocument();
    });

    it('should apply the active class to the active tab', () => {
        render(<Tabs tabs={tabs} />);
        const tab1 = screen.getByText('Tab 1');
        const tab2 = screen.getByText('Tab 2');

        expect(tab1).toHaveClass('active');
        expect(tab2).not.toHaveClass('active');

        fireEvent.click(tab2);

        expect(tab1).not.toHaveClass('active');
        expect(tab2).toHaveClass('active');
    });
});