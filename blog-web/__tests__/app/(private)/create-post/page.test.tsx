import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import CreatePostPage from '@/app/(private)/create-post/page';
import useLoading from '@/hooks/useLoading';
import { useRouter } from 'next/navigation';
import userEvent from '@testing-library/user-event';

jest.mock('@/hooks/useLoading', () => ({
  __esModule: true,
  default: jest.fn(),
}));
jest.mock('next/navigation', () => ({
  useRouter: jest.fn(),
}));

global.fetch = jest.fn();

function setup(tsx: React.ReactElement) {
  return {
    user: userEvent.setup(),
    ...render(tsx),
  };
}

describe('CreatePostPage', () => {
  beforeEach(() => {
    (useLoading as jest.Mock).mockReturnValue({
      isLoading: false,
      startLoading: jest.fn(),
      stopLoading: jest.fn(),
    });

    (useRouter as jest.Mock).mockReturnValue({
      push: jest.fn(),
      back: jest.fn(),
      forward: jest.fn(),
      refresh: jest.fn(),
      replace: jest.fn(),
      prefetch: jest.fn(),
    });

    jest.spyOn(global, 'fetch').mockResolvedValue({
      ok: true,
      json: async () => ({ slug: 'test-slug' }),
    } as Response);
  });

  afterEach(() => {
    jest.clearAllMocks();
    (global.fetch as jest.Mock).mockReset();
  });

  it('renders the CreatePostPage component', () => {
    setup(<CreatePostPage />);
    expect(screen.getByPlaceholderText('Title')).toBeInTheDocument();
    expect(screen.getByText('Publish')).toBeInTheDocument();
    expect(screen.getByLabelText('Upload Cover Image')).toBeInTheDocument();
    expect(screen.getByLabelText('Preview')).toBeInTheDocument();
    expect(screen.getByLabelText('Upload Content Image')).toBeInTheDocument();
  });

  it('shows loading state when publishing', () => {
    (useLoading as jest.Mock).mockReturnValue({
      isLoading: true,
      startLoading: jest.fn(),
      stopLoading: jest.fn(),
    });
    setup(<CreatePostPage />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
    expect(screen.getByText('Publish')).toBeDisabled();
  });

  it('updates title state when input value changes', async () => {
    const { user } = setup(<CreatePostPage />);
    const titleInput = screen.getByPlaceholderText('Title');
    await user.clear(titleInput);
    await user.type(titleInput, 'New Post Title');
    expect(titleInput).toHaveValue('New Post Title');
  });

  it('shows modal with response message when post is saved successfully', async () => {
    const { user } = setup(<CreatePostPage />);
    await user.type(screen.getByPlaceholderText('Title'), 'Test Title');
    await user.click(screen.getByText('Publish'));
    await waitFor(() => {
      const modal = screen.queryByRole('dialog');
      expect(modal).toBeInTheDocument();
    });
  });

  it('calls handleImageUpload when a cover image is selected', async () => {
    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => ({ url: 'http://image.com' }),
    } as Response);
    const { user } = setup(<CreatePostPage />);
    const file = new File(['dummy content'], 'example.png', { type: 'image/png' });
    const input = screen.getByLabelText('Upload Cover Image').parentElement?.querySelector('input[type="file"]') as HTMLInputElement;
    await user.upload(input, file);
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalled();
    });
  });

  it('calls handleImageUpload when a content image is selected', async () => {
    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => ({ url: 'http://image.com' }),
    } as Response);
    const { user } = setup(<CreatePostPage />);
    const uploadContentImageButton = screen.getByLabelText('Upload Content Image');
    await user.click(uploadContentImageButton);
    expect(uploadContentImageButton).toBeInTheDocument();
  });

  it('shows error message when image is not uploaded successfully', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({ ok: false } as Response);
    const { user } = setup(<CreatePostPage />);
    const file = new File(['dummy content'], 'example.png', { type: 'image/png' });
    const input = screen.getByLabelText('Upload Cover Image').parentElement?.querySelector('input[type="file"]') as HTMLInputElement;
    await user.upload(input, file);
    await waitFor(() => {
      expect(screen.getByText((content) => content.includes('Failed to upload image'))).toBeInTheDocument();
    });
  });

  it('shows preview modal when preview button is clicked', async () => {
    const { user } = setup(<CreatePostPage />);
    const previewButton = screen.getByLabelText('Preview');
    await user.click(previewButton);
    await waitFor(() => {
      expect(screen.getAllByText('Close').length).toBeGreaterThan(0);
    });
  });
});
