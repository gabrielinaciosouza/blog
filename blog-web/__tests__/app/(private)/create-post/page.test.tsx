import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import CreatePostPage from '@/app/(private)/create-post/page';
import useLoading from '@/hooks/useLoading';
import { useRouter } from 'next/navigation';
import userEvent from '@testing-library/user-event';

jest.mock('@/hooks/useLoading');
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
  let mockUseLoading: jest.MockedFunction<typeof useLoading>;
  let mockUseRouter: jest.MockedFunction<typeof useRouter>;

  beforeEach(() => {
    mockUseLoading = useLoading as jest.MockedFunction<typeof useLoading>;
    mockUseRouter = useRouter as jest.MockedFunction<typeof useRouter>;

    mockUseLoading.mockReturnValue({
      isLoading: false,
      startLoading: jest.fn(),
      stopLoading: jest.fn(),
    });

    mockUseRouter.mockReturnValue({
      push: jest.fn(),
      back: jest.fn(),
      forward: jest.fn(),
      refresh: jest.fn(),
      replace: jest.fn(),
      prefetch: jest.fn(),
    });

    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => ({ slug: 'test-slug' }),
    });
  });

  afterEach(() => {

    jest.clearAllMocks();
  });

  it('should render the CreatePostPage component', () => {
    setup(<CreatePostPage />);

    expect(screen.getByPlaceholderText('Title')).toBeInTheDocument();
    expect(document.querySelector("[data-placeholder='Write your story...']")).toBeInTheDocument();
    expect(screen.getByText('Publish')).toBeInTheDocument();
  });

  it('should call handlePublish when the publish button is clicked', async () => {
    const { user } = setup(<CreatePostPage />);

    const publishButton = screen.getByText('Publish');
    await user.click(publishButton);

    await waitFor(() => {
      expect(screen.getByText('Title and content are required')).toBeInTheDocument();
      expect(screen.getByText('close')).toBeInTheDocument();
    });
  });

  it('should show loading state when publishing', async () => {
    mockUseLoading.mockReturnValue({
      ...mockUseLoading(),
      isLoading: true,
    });

    const { user } = setup(<CreatePostPage />);

    const loading = screen.getByText('Loading...');
    const publishButton = screen.getByText('Publish');
    expect(loading).toBeInTheDocument();
    expect(publishButton).toBeDisabled();
  });

  it('should show modal with response message when post is saved successfully', async () => {
    const { user } = setup(<CreatePostPage />);

    await user.click(screen.getByPlaceholderText('Title'));
    await user.type(screen.getByPlaceholderText('Title'), 'Test Title');

    const contentInput = document.querySelector("[data-placeholder='Write your story...']") as HTMLElement;
    await user.type(contentInput, 'Test Content');

    const publishButton = screen.getByText('Publish');
    await user.click(publishButton);

    await waitFor(() => {
      expect(screen.getByText('Post saved successfully')).toBeInTheDocument();
    });
  });

  it('should update title state when input value changes', async () => {
    const { user } = setup(<CreatePostPage />);

    const titleInput = screen.getByPlaceholderText('Title');
    await user.clear(titleInput);
    await user.type(titleInput, 'New Post Title');

    expect(titleInput).toHaveValue('New Post Title');
  });

  it('should toggle the open state when the plus button is clicked', async () => {
    const { user } = setup(<CreatePostPage />);

    const plusButton = screen.getByLabelText('Plus');
    await user.click(plusButton);

    expect(screen.getByLabelText('Image')).toBeInTheDocument();
    expect(screen.getByLabelText('Preview')).toBeInTheDocument();
    expect(screen.getByLabelText('Upload Image')).toBeInTheDocument();

    await user.click(plusButton);
    expect(screen.queryByAltText('Image')).not.toBeInTheDocument();
  });

  it('should call handleImageUpload when an image is selected', async () => {
    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => ({ url: 'http://image.com' }),
    });
    const { user } = setup(<CreatePostPage />);

    const file = new File(['dummy content'], 'example.png', { type: 'image/png' });

    const plusButton = screen.getByLabelText('Plus');
    await user.click(plusButton);

    const image = screen.getByLabelText('Image') as HTMLInputElement;
    await user.click(image);

    const input = screen.getByRole('coverImageUpload') as HTMLInputElement;
    await user.upload(input, file);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalled();
    });
  });

  it('should insert image into editor when handleImageInsert is called', async () => {
    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => ({ url: 'http://image.com' }),
    });
    const { user } = setup(<CreatePostPage />);

    const file = new File(['dummy content'], 'example.png', { type: 'image/png' });
    const plusButton = screen.getByLabelText('Plus');
    await user.click(plusButton);

    const image = screen.getByLabelText('Upload Image') as HTMLInputElement;
    await user.click(image);

    const input = screen.getByRole('contentImageUpload') as HTMLInputElement;
    await user.upload(input, file);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalled();
    });
  });

  it('should call router.push with the correct slug when post is saved successfully', async () => {
    const { user } = setup(<CreatePostPage />);

    await user.click(screen.getByPlaceholderText('Title'));
    await user.type(screen.getByPlaceholderText('Title'), 'Test Title');

    const contentInput = document.querySelector("[data-placeholder='Write your story...']") as HTMLElement;
    await user.type(contentInput, 'Test Content');

    const publishButton = screen.getByText('Publish');
    await user.click(publishButton);

    const okButton = screen.getByText('close');
    await user.click(okButton);

    await waitFor(() => {
      expect(mockUseRouter().push).toHaveBeenCalledWith('/posts/test-slug');
    });
  });

  it('should show error message when post is not saved successfully', async () => {
    (global.fetch as jest.Mock).mockRejectedValue(new Error('Failed to save post'));
    const { user } = setup(<CreatePostPage />);

    await user.click(screen.getByPlaceholderText('Title'));
    await user.type(screen.getByPlaceholderText('Title'), 'Test Title');

    const contentInput = document.querySelector("[data-placeholder='Write your story...']") as HTMLElement;
    await user.type(contentInput, 'Test Content');

    const publishButton = screen.getByText('Publish');
    await user.click(publishButton);

    await waitFor(() => {
      expect(screen.getByText('Failed to save post')).toBeInTheDocument();
    });
  });

  it('should show error message when image is not uploaded successfully', async () => {
    (global
      .fetch as jest.Mock)
      .mockResolvedValueOnce({
        ok: false,
      });
    const { user } = setup(<CreatePostPage />);
    const file = new File(['dummy content'], 'example.png', { type: 'image/png' });
    const plusButton = screen.getByLabelText('Plus');
    await user.click(plusButton);
    const input = screen.getByRole('coverImageUpload') as HTMLInputElement;
    await user.upload(input, file);

    await waitFor(() => {
      expect(screen.getByText('Failed to upload image')).toBeInTheDocument();
    });
  });

  it('should show preview modal', async () => {
    const { user } = setup(<CreatePostPage />);
    const plusButton = screen.getByLabelText('Plus');
    await user.click(plusButton);

    const preview = screen.getByLabelText('Preview') as HTMLInputElement;
    await user.click(preview);

    expect(screen.getByText('close')).toBeInTheDocument();
  });
});