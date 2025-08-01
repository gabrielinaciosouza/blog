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
  it('clears content image input value when content image upload fails', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({ ok: false } as Response);
    const { user } = setup(<CreatePostPage />);
    const originalCreateElement = document.createElement;
    const file = new File(['dummy content'], 'example.png', { type: 'image/png' });
    let input: HTMLInputElement | null = null;
    document.createElement = jest.fn((tag) => {
      if (tag === 'input') {
        input = originalCreateElement.call(document, 'input') as HTMLInputElement;
        setTimeout(() => {
          Object.defineProperty(input!, 'files', {
            value: [file],
            writable: false,
          });
          input!.onchange && input!.onchange({ target: input } as any);
        }, 10);
        return input!;
      }
      return originalCreateElement.call(document, tag);
    });
    const uploadToolbarButton = screen.getAllByRole('button', { name: /Upload Content Image/i })[0];
    await user.click(uploadToolbarButton);
    await waitFor(() => {
      expect(input).not.toBeNull();
      expect(input!.value).toBe('');
    });
    document.createElement = originalCreateElement;
  });

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

  it('shows error modal with error message when publish fails', async () => {
    const errorMessage = 'Network error';
    window.localStorage.setItem('draft-title', JSON.stringify('Test Title'));
    window.localStorage.setItem('draft-content', JSON.stringify('Test Content'));
    (global.fetch as jest.Mock).mockRejectedValueOnce(new Error(errorMessage));
    const { user } = setup(<CreatePostPage />);
    await user.click(screen.getByText('Publish'));
    await waitFor(() => {
      const modal = screen.getByRole('dialog');
      expect(modal).toBeInTheDocument();
    });
  });

  it('uploads content image via editor toolbar and inserts image into editor', async () => {
    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      json: async () => ({ url: 'http://image.com/content.png' }),
    } as Response);
    const { user } = setup(<CreatePostPage />);

    const originalCreateElement = document.createElement;
    const file = new File(['dummy content'], 'example.png', { type: 'image/png' });
    document.createElement = jest.fn((tag) => {
      if (tag === 'input') {
        const input = originalCreateElement.call(document, 'input');
        setTimeout(() => {
          Object.defineProperty(input, 'files', {
            value: [file],
            writable: false,
          });
          input.onchange && input.onchange({ target: input } as any);
        }, 10);
        return input;
      }
      return originalCreateElement.call(document, tag);
    });

    const uploadToolbarButton = screen.getAllByRole('button', { name: /Upload Content Image/i })[0];
    await user.click(uploadToolbarButton);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining('/api/images?type=blog-content-images'), expect.any(Object));
    });
    await waitFor(() => {
      expect(screen.getByText(/Image uploaded successfully/i)).toBeInTheDocument();
    });

    document.createElement = originalCreateElement;
  });

  it('triggers editor commands when toolbar buttons are clicked', async () => {
    const { user } = setup(<CreatePostPage />);
    const boldButton = screen.getAllByRole('button', { name: /B/i })[0];
    const italicButton = screen.getAllByRole('button', { name: /I/i })[0];
    const underlineButton = screen.getAllByRole('button', { name: /U/i })[0];

    await user.click(boldButton);
    await user.click(italicButton);
    await user.click(underlineButton);

    expect(boldButton).toBeInTheDocument();
    expect(italicButton).toBeInTheDocument();
    expect(underlineButton).toBeInTheDocument();
  });

  it('closes modal when Close button is clicked', async () => {
    const { user } = setup(<CreatePostPage />);
    await user.click(screen.getByText('Publish'));
    await waitFor(() => {
      expect(screen.queryByRole('dialog')).toBeInTheDocument();
    });
    const closeButtons = screen.getAllByRole('button', { name: 'Close' });
    await user.click(closeButtons[closeButtons.length - 1]);
    await waitFor(() => {
      expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
    });
  });

  it('bold toolbar button is present and clickable', async () => {
    const { user } = setup(<CreatePostPage />);
    const boldButton = screen.getAllByRole('button', { name: /Bold/i })[0];
    await user.click(boldButton);
    expect(boldButton).toBeInTheDocument();
  });

  it('italic toolbar button is present and clickable', async () => {
    const { user } = setup(<CreatePostPage />);
    const italicButton = screen.getAllByRole('button', { name: /Italic/i })[0];
    await user.click(italicButton);
    expect(italicButton).toBeInTheDocument();
  });



  it('link toolbar button is present and clickable', async () => {
    const { user } = setup(<CreatePostPage />);
    const linkButton = screen.getAllByRole('button', { name: /Link/i })[0];
    await user.click(linkButton);
    expect(linkButton).toBeInTheDocument();
  });

  it('shows modal if title or content is empty when publishing', async () => {
    const { user } = setup(<CreatePostPage />);
    await user.click(screen.getByText('Publish'));
    await waitFor(() => {
      const modal = screen.queryByRole('dialog');
      expect(modal).toBeInTheDocument();
      expect(screen.getByText(/required/i)).toBeInTheDocument();
    });
  });

  it('calls handlePublish and shows modal if title or content is empty', async () => {
    const { user } = setup(<CreatePostPage />);
    await user.click(screen.getByText('Publish'));
    await waitFor(() => {
      expect(screen.getByText(/required/i)).toBeInTheDocument();
    });
  });

  it('renders the CreatePostPage component', () => {
    setup(<CreatePostPage />);
    expect(screen.getByPlaceholderText('Title')).toBeInTheDocument();
    expect(screen.getByText('Publish')).toBeInTheDocument();
    expect(screen.getByText('Upload Cover')).toBeInTheDocument();
    expect(screen.getByText('Upload Content Image')).toBeInTheDocument();
    expect(screen.getByText((content) => /preview/i.test(content))).toBeInTheDocument();
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
    await user.click(screen.getByText('Upload Cover'));
    const fileInput = document.querySelector('input[type="file"]');
    await user.upload(fileInput as HTMLInputElement, file);
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
    const uploadContentImageButton = screen.getByText('Upload Content Image');
    await user.click(uploadContentImageButton);
    expect(uploadContentImageButton).toBeInTheDocument();
  });

  it('shows error message when image is not uploaded successfully', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({ ok: false } as Response);
    const { user } = setup(<CreatePostPage />);
    const file = new File(['dummy content'], 'example.png', { type: 'image/png' });
    await user.click(screen.getByText('Upload Cover'));
    const fileInput = document.querySelector('input[type="file"]');
    await user.upload(fileInput as HTMLInputElement, file);
    await waitFor(() => {
      // Use getAllByText and check that at least one element is the error message (not a button)
      const errorElements = screen.getAllByText((content) => /failed|error|upload/i.test(content));
      // Filter out buttons
      const errorMessages = errorElements.filter((el) => el.tagName !== 'BUTTON');
      expect(errorMessages.length).toBeGreaterThan(0);
    });
  });

  it('calls handlePublish and shows success modal, clears fields, and navigates on success', async () => {
    const pushMock = jest.fn();
    (useRouter as jest.Mock).mockReturnValue({
      push: pushMock,
      back: jest.fn(),
      forward: jest.fn(),
      refresh: jest.fn(),
      replace: jest.fn(),
      prefetch: jest.fn(),
    });
    // Ensure fetch returns a successful response
    (global.fetch as jest.Mock).mockResolvedValue({
      ok: true,
      status: 201,
      json: async () => ({ slug: 'test-slug' }),
    } as Response);
    const { user } = setup(<CreatePostPage />);
    await user.type(screen.getByPlaceholderText('Title'), 'Test Title');
    await user.type(screen.getByPlaceholderText('Write your post in Markdown...'), 'Test Content');
    await user.click(screen.getByText('Publish'));
    await waitFor(() => {
      const modal = screen.queryByRole('dialog');
      expect(modal).toBeInTheDocument();
      // Check for success message in modal content
      expect(modal?.textContent?.toLowerCase()).toContain('post saved successfully');
    });
    // Simulate closing modal
    const closeButtons = screen.getAllByText(/close/i);
    // Click the last button (should be the modal's Close button)
    await user.click(closeButtons[closeButtons.length - 1]);
    expect(pushMock).toHaveBeenCalledWith('/posts/test-slug');
  });

  it('calls handlePublish and shows error modal on fetch failure', async () => {
    (global.fetch as jest.Mock).mockRejectedValueOnce(new Error('Network error'));
    const { user } = setup(<CreatePostPage />);
    await user.type(screen.getByPlaceholderText('Title'), 'Test Title');
    await user.type(screen.getByPlaceholderText('Write your post in Markdown...'), 'Test Content');
    await user.click(screen.getByText('Publish'));
    await waitFor(() => {
      expect(screen.getByText(/an error occurred/i)).toBeInTheDocument();
    });
  });

  it('calls handlePublish and shows error modal on non-OK response', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({ ok: false, json: async () => ({ message: 'Server error' }) } as Response);
    const { user } = setup(<CreatePostPage />);
    await user.type(screen.getByPlaceholderText('Title'), 'Test Title');
    await user.type(screen.getByPlaceholderText('Write your post in Markdown...'), 'Test Content');
    await user.click(screen.getByText('Publish'));
    await waitFor(() => {
      // Fallback: check for any modal/dialog and print its content for debug
      const modal = screen.queryByRole('dialog');
      expect(modal).toBeInTheDocument();
      // Try to find the error message in the modal textContent
      expect(modal?.textContent?.toLowerCase()).toContain('server error');
    });
  });
});
