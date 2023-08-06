package com.learn.guava.cache;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReferenceExample {
    public static void main(String[] args) throws InterruptedException {
          // strong ref
        int counter = 0;
//
//        final List<Ref> container = new ArrayList<>();
//
//        while (true) {
//            int current = counter ++;
//            container.add(new Ref(current));
//            System.out.println("The " + current + " ref will be insert into container");
//            TimeUnit.MILLISECONDS.sleep(500);
//        }

        SoftReference<Ref> softReference = new SoftReference<Ref>(new Ref(0));

        /**
         * detected the JVM process memory have little space try to GC soft ref
         */
//        final List<SoftReference<Ref>> container = new ArrayList<>();
//        while (true) {
//            int current = counter ++;
//            container.add(new SoftReference<Ref>(new Ref(current)));
//            System.out.println("The " + current + " ref will be insert into container");
//            TimeUnit.MILLISECONDS.sleep(500);
//        }

        /**
         * weak ref
         * weak ref will be collected when gc
         */

        WeakReference<Ref> weakReference = new WeakReference<>(new Ref(0));

        Ref ref = new Ref(19);
        ReferenceQueue queue = new ReferenceQueue<>();
        MyPhantomReference reference = new MyPhantomReference(ref, queue, 19);
        ref = null;

        System.out.println(reference.get());
        System.gc();
        Reference object = queue.remove();
        ((MyPhantomReference) object).doAction();
    }

    private static class MyPhantomReference extends PhantomReference<Object> {
        private int index;

        public MyPhantomReference(Object referent, ReferenceQueue<? super Object> q, int index) {
            super(referent, q);
            this.index = index;
        }

        public void doAction() {

            System.out.println("the object [" + index + "] is GC.");
        }
    }

    static class Ref {
        private byte[] data = new byte[1024 * 1024];

        private final int index;

        public Ref(int index) {
            this.index = index;
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println("The index: [" + index +"] will be GC.");
        }
    }
}
