import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import CreatePostPage from '@/app/create-post/page';
import { usePublishPost } from '@/hooks/usePublishPost';

jest.mock('@/hooks/usePublishPost');

describe('CreatePostPage', () => {
  let mockUsePublishPost: jest.MockedFunction<typeof usePublishPost>;

  beforeEach(() => {
    mockUsePublishPost = usePublishPost as jest.MockedFunction<typeof usePublishPost>;
    mockUsePublishPost.mockReturnValue({
      title: '',
      setTitle: jest.fn(),
      content: '',
      setContent: jest.fn(),
      loading: false,
      responseMessage: '',
      showModal: false,
      handlePublish: jest.fn(),
      handleCloseModal: jest.fn(),
    });
  });

  it('should render the CreatePostPage component', () => {
    render(<CreatePostPage />);

    expect(screen.getByPlaceholderText('Title')).toBeInTheDocument();
    expect(document.querySelector("[data-placeholder='Write your story...']")).toBeInTheDocument();
    expect(screen.getByText('Publish')).toBeInTheDocument();
  });

  it('should call handlePublish when the publish button is clicked', () => {
    render(<CreatePostPage />);

    const publishButton = screen.getByText('Publish');
    fireEvent.click(publishButton);

    expect(mockUsePublishPost().handlePublish).toHaveBeenCalled();
  });

  it('should show loading state when publishing', () => {
    mockUsePublishPost.mockReturnValue({
      ...mockUsePublishPost(),
      loading: true,
    });

    render(<CreatePostPage />);

    const publishButton = screen.getByText('Publishing...');
    expect(publishButton).toBeInTheDocument();
    expect(publishButton).toBeDisabled();
  });

  it('should show modal with response message when showModal is true', () => {
    mockUsePublishPost.mockReturnValue({
      ...mockUsePublishPost(),
      showModal: true,
      responseMessage: 'Post saved successfully!',
    });

    render(<CreatePostPage />);

    expect(screen.getByText('Post saved successfully!')).toBeInTheDocument();
    expect(screen.getByText('OK')).toBeInTheDocument();
  });

  it('should call handleCloseModal when the OK button in the modal is clicked', () => {
    mockUsePublishPost.mockReturnValue({
      ...mockUsePublishPost(),
      showModal: true,
    });

    render(<CreatePostPage />);

    const okButton = screen.getByText('OK');
    fireEvent.click(okButton);

    expect(mockUsePublishPost().handleCloseModal).toHaveBeenCalled();
  });

  it('should update title state when input value changes', () => {
    render(<CreatePostPage />);

    const titleInput = screen.getByPlaceholderText('Title');
    fireEvent.change(titleInput, { target: { value: 'New Post Title' } });

    expect(mockUsePublishPost().setTitle).toHaveBeenCalledWith('New Post Title');
});

it('should toggle the open state when the plus button is clicked', () => {
    render(<CreatePostPage />);

    const plusButton = screen.getByAltText('');
    fireEvent.click(plusButton);

    expect(screen.getByAltText('image')).toBeInTheDocument();
    fireEvent.click(plusButton);
    expect(screen.queryByAltText('image')).not.toBeInTheDocument();
});
});
