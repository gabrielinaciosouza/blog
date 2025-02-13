import React from 'react';
import { render, screen } from '@testing-library/react';
import Footer from '@/components/footer/Footer';

describe('Footer', () => {
  it('should render the logo', () => {
    render(<Footer />);
    expect(screen.getByAltText("Gabriel's blog")).toBeInTheDocument();
  });

  it('should render the year', () => {
    const currentYear = new Date().getFullYear();
    render(<Footer />);
    expect(screen.getByText(`Gabriel, ${currentYear}`)).toBeInTheDocument();
  });

  it('should render the links', () => {
    render(<Footer />);
    expect(screen.getByText("Homepage")).toBeInTheDocument();
    expect(screen.getByText("Blog")).toBeInTheDocument();
    expect(screen.getByText("About")).toBeInTheDocument();
    expect(screen.getByText("Contact")).toBeInTheDocument();
  });

  it('should render the copyright text', () => {
    const currentYear = new Date().getFullYear();
    render(<Footer />);
    expect(screen.getByText(`© ${currentYear} Gabriel Inacio. All rights reserved.`)).toBeInTheDocument();
  });

  it('should render the social media icons', () => {
    render(<Footer />);
    expect(screen.getByAltText("Facebook")).toBeInTheDocument();
    expect(screen.getByAltText("Instagram")).toBeInTheDocument();
    expect(screen.getByAltText("Tiktok")).toBeInTheDocument();
    expect(screen.getByAltText("Youtube")).toBeInTheDocument();
  });
});