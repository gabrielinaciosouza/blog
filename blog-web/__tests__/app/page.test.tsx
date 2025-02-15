import React, { Suspense } from 'react';
import { render, screen, waitFor, act } from '@testing-library/react';
import Home from '@/app/page';
import Featured from '@/components/featured/Featured';
import PostList from '@/components/postList/PostList';

jest.mock('@/components/featured/Featured');
jest.mock('@/components/postList/PostList');

const MockFeatured = Featured as jest.MockedFunction<typeof Featured>;
const MockPostList = PostList as jest.MockedFunction<typeof PostList>;

describe('Home', () => {
  beforeEach(() => {
    MockFeatured.mockImplementation(() => <div>Mock Featured</div>);
    MockPostList.mockImplementation(() => Promise.resolve(<div>Mock PostList</div>));
  });

  it('should render the Featured and PostList component and container', async () => {
    await act(async () => {
      render(
        <Suspense fallback={<div>Loading...</div>}>
          <Home />
        </Suspense>
      );
    });

    await waitFor(() => {
      expect(screen.getByText('Mock Featured')).toBeInTheDocument();
      expect(screen.getByText('Mock PostList')).toBeInTheDocument();
      expect(screen.getByRole('main')).toBeInTheDocument();
    });
  });
});
