import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import SinglePage from '@/app/posts/[slug]/page';
import { getPostBySlug } from '@/services/postService';

jest.mock('@/services/postService');

const mockedGetPostBySlug = getPostBySlug as jest.MockedFunction<typeof getPostBySlug>;

describe('SinglePage', () => {
  it('should render the post content when post is found', () => {
    const post = {
      postId: '1',
      title: 'Test Title',
      content: '<p>This is the content of the post.</p>',
      creationDate: '2021-12-12T00:00:00Z',
      slug: 'test-title',
    };

    mockedGetPostBySlug.mockResolvedValueOnce(post);

    const { container } = render(<SinglePage params={{ slug: 'test-title' }} />);

    waitFor(() => {
      expect(screen.getByText('Test Title')).toBeInTheDocument();
      expect(screen.getByText('Gabriel Inacio')).toBeInTheDocument();
      expect(screen.getByText('12/12/2021')).toBeInTheDocument();
      const paragraph = container.querySelector('p');
      expect(paragraph?.innerHTML).toBe('This is the content of the post.');
    });
  });

  it('should render error message when post is not found', () => {
    mockedGetPostBySlug.mockRejectedValueOnce(new Error('Post not found'));

    render(<SinglePage params={{ slug: 'non-existent-post' }} />);

    waitFor(() => {
      expect(screen.getByText('Post Not Found')).toBeInTheDocument();
      expect(screen.getByText('Post not found')).toBeInTheDocument();
    });
  });
});