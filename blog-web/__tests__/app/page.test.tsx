import React from 'react';
import { render, screen } from '@testing-library/react';
import Home from '@/app/page';
import Featured from '@/components/featured/Featured';

jest.mock('@/components/featured/Featured');

const MockFeatured = Featured as jest.MockedFunction<typeof Featured>;

describe('Home', () => {
  beforeEach(() => {
    MockFeatured.mockImplementation(() => <div>Mock Featured</div>);
  });

  it('should render the Featured component and container', () => {
    render(<Home />);

    expect(screen.getByText('Mock Featured')).toBeInTheDocument();
    expect(screen.getByRole('main')).toBeInTheDocument();
  });
});