
import React from 'react';
import { render, screen } from '@testing-library/react';
import Featured from '@/components/featured/Featured';

jest.mock('next/image', () => (props: any) => {
  // eslint-disable-next-line jsx-a11y/alt-text
  return <img {...props} />;
});

jest.mock('@radix-ui/react-avatar', () => {
  const React = require('react');
  return {
    __esModule: true,
    Root: ({ children, ...props }: any) => <span {...props}>{children}</span>,
    Image: ({ alt, src, ...props }: any) => <img alt={alt} src={src || '/profile-picture.png'} data-testid="avatar-image" {...props} />, // AvatarImage
    Fallback: () => null,
  };
});

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