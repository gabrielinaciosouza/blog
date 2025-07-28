import React from 'react';
import { render, screen } from '@testing-library/react';
import Footer from '@/components/footer/Footer';

describe('Footer', () => {
  it('should render the copyright text', () => {
    const currentYear = new Date().getFullYear();
    render(<Footer />);
    expect(screen.getByText(`© ${currentYear} Gabriel Inacio. All rights reserved.`)).toBeInTheDocument();
  });

  it('should render the social media icons', () => {
    render(<Footer />);
    expect(screen.getByLabelText('LinkedIn')).toBeInTheDocument();
    expect(screen.getByLabelText('Github')).toBeInTheDocument();
    expect(screen.getByLabelText("Youtube")).toBeInTheDocument();
  });
});