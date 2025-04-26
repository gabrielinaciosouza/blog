import React from 'react';
import { render, screen } from '@testing-library/react';
import Navbar from '@/components/navbar/Navbar';
import AuthLinks from '@/components/authLinks/AuthLinks';

jest.mock('@/components/authLinks/AuthLinks');

const MockAuthLinks = AuthLinks as jest.MockedFunction<typeof AuthLinks>;

describe('Navbar', () => {
  beforeEach(() => {
    MockAuthLinks.mockImplementation(({ authStatus }) => <div>Mock AuthLinks - {authStatus}</div>);
  });

  it('should render the nav element with role navigation', () => {
    render(<Navbar />);
    const nav = screen.getByRole('navigation', { name: /main navigation/i });
    expect(nav).toBeInTheDocument();
  });

  it('should render the logo', () => {
    render(<Navbar />);
    expect(screen.getByAltText("logo2")).toBeInTheDocument();
  });

  it('should render the text logo', () => {
    render(<Navbar />);
    expect(screen.getByText("GABRIEL INACIO")).toBeInTheDocument();
  });

  it('should render the links as list items', () => {
    render(<Navbar />);
    const list = screen.getByRole('list');
    expect(list).toBeInTheDocument();
    expect(screen.getByText("Homepage").closest('li')).toBeInTheDocument();
    expect(screen.getByText("Contact").closest('li')).toBeInTheDocument();
    expect(screen.getByText("About").closest('li')).toBeInTheDocument();
  });

  it('should render the AuthLinks component with authenticated status', () => {
    render(<Navbar authStatus="authenticated" />);
    expect(screen.getByText("Mock AuthLinks - authenticated")).toBeInTheDocument();
  });

  it('should render the AuthLinks component with notauthenticated status', () => {
    render(<Navbar authStatus="notauthenticated" />);
    expect(screen.getByText("Mock AuthLinks - notauthenticated")).toBeInTheDocument();
  });
});