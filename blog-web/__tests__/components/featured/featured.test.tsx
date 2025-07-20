import React from 'react';
import { render, screen } from '@testing-library/react';
import Featured from '@/components/featured/Featured';

describe('Featured', () => {
  it('should render the title', () => {
    render(<Featured />);
    expect(screen.getByText("Hi, I'm Gabriel")).toBeInTheDocument();
  });

  it('should render the subtitle', () => {
    render(<Featured />);
    expect(screen.getByTestId('subtitle')).toBeInTheDocument();
  });

  it('should render the image', () => {
    render(<Featured />);
    expect(screen.getByAltText("Gabriel's profile picture")).toBeInTheDocument();
  });
});