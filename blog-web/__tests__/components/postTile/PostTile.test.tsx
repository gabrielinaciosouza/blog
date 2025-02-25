import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import PostTile from '@/components/postTile/PostTile';
import Post from '@/models/post';

describe('PostTile', () => {
    const post: Post = {
        postId: '1',
        title: 'Test Post',
        creationDate: '2025-02-25',
        slug: 'test-post',
        content: 'Test Content',
        coverImage: 'test.jpg',
    };

    it('should render the post details', () => {
        render(<PostTile post={post} />);
        expect(screen.getByText('Test Post')).toBeInTheDocument();
        expect(screen.getByText('2025-02-25')).toBeInTheDocument();
        expect(screen.getByText('test-post')).toBeInTheDocument();
    });

    it('should call onEdit when the edit button is clicked', () => {
        const onEdit = jest.fn();
        render(<PostTile post={post} onEdit={onEdit} />);
        fireEvent.click(screen.getByLabelText('Edit'));
        expect(onEdit).toHaveBeenCalledWith('1');
    });

    it('should call onDelete when the delete button is clicked', () => {
        const onDelete = jest.fn();
        render(<PostTile post={post} onDelete={onDelete} />);
        fireEvent.click(screen.getByLabelText('Delete'));
        expect(onDelete).toHaveBeenCalledWith('1');
    });

    it('should call onRestore when the restore button is clicked', () => {
        const onRestore = jest.fn();
        render(<PostTile post={post} onRestore={onRestore} />);
        fireEvent.click(screen.getByLabelText('Restore'));
        expect(onRestore).toHaveBeenCalledWith('1');
    });

    it('should call onOpen when the open button is clicked', () => {
        const onOpen = jest.fn();
        render(<PostTile post={post} onOpen={onOpen} />);
        fireEvent.click(screen.getByLabelText('Open'));
        expect(onOpen).toHaveBeenCalledWith('1');
    });
});