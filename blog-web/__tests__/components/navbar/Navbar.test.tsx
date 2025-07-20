import React from 'react';
import { render, screen } from '@testing-library/react';
import Navbar from '@/components/navbar/Navbar';

describe('Navbar', () => {

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
    expect(screen.getByText("Gabriel's Blog")).toBeInTheDocument();
  });

  it('should render the links as list items', () => {
    render(<Navbar />);
    const list = screen.getByRole('list');
    expect(list).toBeInTheDocument();
    expect(screen.getByText("Homepage").closest('li')).toBeInTheDocument();
    expect(screen.getByText("Contact").closest('li')).toBeInTheDocument();
    expect(screen.getByText("About").closest('li')).toBeInTheDocument();
  });
});