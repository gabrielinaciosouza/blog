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
    expect(screen.getByLabelText("Facebook")).toBeInTheDocument();
    expect(screen.getByLabelText("Instagram")).toBeInTheDocument();
    expect(screen.getByLabelText("Tiktok")).toBeInTheDocument();
    expect(screen.getByLabelText("Youtube")).toBeInTheDocument();
  });
});