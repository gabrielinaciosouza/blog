import { uploadImage } from '@/services/imageService'
import { NextRequest, NextResponse } from 'next/server';

export async function POST(req: NextRequest) {

  try {
    const { searchParams } = new URL(req.url);
    const type = searchParams.get("type");

    const formData = await req.formData();

    formData.append("bucketName", type!);
    const blogImage = await uploadImage(formData);
    return NextResponse.json(blogImage)
  }
  catch (err) {
    console.log(err);
    return NextResponse.json({ message: "Something went wrong!" }, { status: 500 });
  }
}