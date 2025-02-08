import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import AuthLinks from '../../src/components/authLinks/AuthLinks';

describe('AuthLinks', () => {
    it('should render login link when status is not authenticated', () => {
        render(<AuthLinks authStatus='notauthenticated' />);

        expect(screen.getAllByText('Login')[0]).toBeInTheDocument();
    });

    it('should render write and logout links when status is authenticated', () => {
        render(<AuthLinks authStatus='authenticated' />);

        expect(screen.getByText('Write')).toBeInTheDocument();
        expect(screen.getByText('Logout')).toBeInTheDocument();
    });

    it('should toggle responsive menu when burger icon is clicked', () => {
        render(<AuthLinks authStatus='notauthenticated' />);

        const burgerIcon = screen.getByRole('button');
        fireEvent.click(burgerIcon);

        expect(screen.getByText('Homepage')).toBeInTheDocument();
        expect(screen.getByText('Contact')).toBeInTheDocument();
        expect(screen.getByText('About')).toBeInTheDocument();
    });

    it('should render login link in responsive menu when status is not authenticated', () => {
        render(<AuthLinks authStatus='notauthenticated' />);


        const burgerIcon = screen.getByRole('button');
        fireEvent.click(burgerIcon);

        expect(screen.getAllByText('Login')[0]).toBeInTheDocument();

    });

    it('should render write and logout links in responsive menu when status is authenticated', () => {
        render(<AuthLinks authStatus="authenticated" />);

        const burgerIcon = screen.getByRole('button');
        fireEvent.click(burgerIcon);

        expect(screen.getAllByText('Write')[0]).toBeInTheDocument();
        expect(screen.getAllByText('Logout')[0]).toBeInTheDocument();
    });
});