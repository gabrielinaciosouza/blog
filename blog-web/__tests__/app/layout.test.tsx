import React from 'react';
import { render, screen } from '@testing-library/react';
import RootLayout from '@/app/layout';
import Navbar from '@/components/navbar/Navbar';
import Footer from '@/components/footer/Footer';

jest.mock('@/components/navbar/Navbar');
jest.mock('@/components/footer/Footer');

const MockNavbar = Navbar as jest.MockedFunction<typeof Navbar>;
const MockFooter = Footer as jest.MockedFunction<typeof Footer>;

describe('RootLayout', () => {
  beforeEach(() => {
    MockNavbar.mockImplementation(() => <div>Mock Navbar</div>);
    MockFooter.mockImplementation(() => <div>Mock Footer</div>);
  });

  it('should render the Navbar, children, and Footer', () => {
    render(
      <RootLayout>
        <div>Test Content</div>
      </RootLayout>
    );

    expect(screen.getByText('Mock Navbar')).toBeInTheDocument();
    expect(screen.getByText('Test Content')).toBeInTheDocument();
    expect(screen.getByText('Mock Footer')).toBeInTheDocument();
  });
});