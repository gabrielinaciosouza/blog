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

  it('should render the logo', () => {
    render(<Navbar />);
    expect(screen.getByAltText("logo2")).toBeInTheDocument();
  });

  it('should render the text logo', () => {
    render(<Navbar />);
    expect(screen.getByText("GABRIEL INACIO")).toBeInTheDocument();
  });

  it('should render the links', () => {
    render(<Navbar />);
    expect(screen.getByText("Homepage")).toBeInTheDocument();
    expect(screen.getByText("Contact")).toBeInTheDocument();
    expect(screen.getByText("About")).toBeInTheDocument();
  });

  it('should render the AuthLinks component with the correct auth status', () => {
    render(<Navbar />);
    expect(screen.getByText("Mock AuthLinks - authenticated")).toBeInTheDocument();
  });
});